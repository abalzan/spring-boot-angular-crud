import {Component, OnInit} from '@angular/core';

import { Country } from './country';
import { CountryService } from './country.service';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-country',
  templateUrl: './country.component.html',
  styleUrls: ['./country.component.css']
})
export class CountryComponent implements OnInit {
  countries: Country[];
  newCountry: Country = new Country();
  editing: boolean = false;
  editingCountry: Country = new Country();

  constructor(
    private countryService: CountryService,
  ) {}

  ngOnInit(): void {
    this.getCountries();
  }

  getCountries(): void {
    this.countryService.getCountries()
      .then(todos => this.countries = todos);
  }

  createCountry(todoForm: NgForm): void {
    this.countryService.createCountry(this.newCountry)
      .then(createCountry => {
        todoForm.reset();
        this.newCountry = new Country();
        this.countries.unshift(createCountry)
      });
  }

  deleteCountry(id: string): void {
    this.countryService.deleteCountry(id)
      .then(() => {
        this.countries = this.countries.filter(todo => todo.id !== id);
      });
  }

  updateCountry(todoData: Country): void {
    console.log(todoData);
    this.countryService.updateCountry(todoData)
      .then(updatedCountry => {
        const existingCountry = this.countries.find(todo => todo.id === updatedCountry.id);
        Object.assign(existingCountry, updatedCountry);
        this.clearEditing();
      });
  }

  editCountry(todoData: Country): void {
    this.editing = true;
    Object.assign(this.editingCountry, todoData);
  }

  clearEditing(): void {
    this.editingCountry = new Country();
    this.editing = false;
  }

}
