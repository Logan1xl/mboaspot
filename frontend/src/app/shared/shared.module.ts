import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AppButtonComponent } from './components/app-button/app-button.component';
import { AppCardComponent } from './components/app-card/app-card.component';
import { AppInputComponent } from './components/app-input/app-input.component';

@NgModule({
  imports: [CommonModule, AppButtonComponent, AppCardComponent, AppInputComponent],
  exports: [AppButtonComponent, AppCardComponent, AppInputComponent]
})
export class SharedModule {}
