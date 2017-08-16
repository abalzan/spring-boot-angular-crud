import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';

import { Country } from './country'

@Injectable()
export class CountryService {
  private baseUrl = 'http://localhost:8080/rest';

  constructor(private http: Http) { }

  getCountries():  Promise<Country[]> {
    return this.http.get(this.baseUrl + '/countries')
      .toPromise()
      .then(response => response.json() as Country[])
      .catch(this.handleError);
  }

  createCountry(countryData: Country): Promise<Country> {
    return this.http.post(this.baseUrl + '/countries/add', countryData)
      .toPromise().then(response => response.json() as Country)
      .catch(this.handleError);
  }

  updateCountry(countryData: Country): Promise<Country> {
    return this.http.put(this.baseUrl + '/countries/update/' + countryData.id, countryData)
      .toPromise()
      .then(response => response.json() as Country)
      .catch(this.handleError);
  }

  deleteCountry(id: string): Promise<any> {
    return this.http.delete(this.baseUrl + '/countries/delete/' + id)
      .toPromise()
      .catch(this.handleError);
  }

  private handleError(error: any): Promise<any> {
    console.error('Some error occured', error);
    return Promise.reject(error.message || error);
  }
}
