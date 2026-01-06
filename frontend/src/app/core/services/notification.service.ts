import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import {MatSnackBarRef} from '@angular/material/snack-bar';

export type NotificationType = 'success' | 'error' | 'info' | 'warning';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(private toastr: ToastrService) { }

  /**
   * Affiche une notification de succès
   */
  success(message: string, title: string = 'Succès'): void {
    this.toastr.success(message, title, {
      timeOut: 3000,
      progressBar: true
    });
  }

  /**
   * Affiche une notification d'erreur
   */
  error(message: string, title: string = 'Erreur'): void {
    this.toastr.error(message, title, {
      timeOut: 4000,
      progressBar: true
    });
  }

  /**
   * Affiche une notification d'information
   */
  info(message: string, title: string = 'Information'): void {
    this.toastr.info(message, title, {
      timeOut: 3000,
      progressBar: true
    });
  }

  /**
   * Affiche une notification d'avertissement
   */
  warning(message: string, title: string = 'Avertissement'): void {
    this.toastr.warning(message, title, {
      timeOut: 3000,
      progressBar: true
    });
  }


  /**
   * Affiche une notification générique
   */
  show(message: string, type: NotificationType = 'info', title?: string): void {
    switch (type) {
      case 'success':
        this.success(message, title || 'Succès');
        break;
      case 'error':
        this.error(message, title || 'Erreur');
        break;
      case 'warning':
        this.warning(message, title || 'Avertissement');
        break;
      case 'info':
      default:
        this.info(message, title || 'Information');
        break;
    }
  }
}
