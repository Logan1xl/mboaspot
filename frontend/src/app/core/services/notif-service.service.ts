import { Injectable } from '@angular/core';
import { MatSnackBar, MatSnackBarConfig, MatSnackBarRef } from '@angular/material/snack-bar';
import {MatDialog, MatDialogActions, MatDialogContent, MatDialogTitle} from '@angular/material/dialog';


export enum NotificationType {
  SUCCESS = 'success',
  ERROR = 'error',
  WARNING = 'warning',
  INFO = 'info'
}

export interface NotificationConfig extends MatSnackBarConfig {
  type?: NotificationType;
  showIcon?: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class NotifService {
  private defaultConfig: NotificationConfig = {
    duration: 4000,
    horizontalPosition: 'end',
    verticalPosition: 'top',
    showIcon: true
  };

  constructor(
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ) {}

  /**
   * Affiche une notification de succès
   */
  success(message: string, action: string = 'OK', config?: NotificationConfig): MatSnackBarRef<any> {
    return this.show(message, action, {
      ...this.defaultConfig,
      ...config,
      type: NotificationType.SUCCESS,
      panelClass: ['notification-success', 'custom-snackbar']
    });
  }

  /**
   * Affiche une notification d'erreur
   */
  error(message: string, action: string = 'OK', config?: NotificationConfig): MatSnackBarRef<any> {
    return this.show(message, action, {
      ...this.defaultConfig,
      ...config,
      type: NotificationType.ERROR,
      duration: 6000,
      panelClass: ['notification-error', 'custom-snackbar']
    });
  }

  /**
   * Affiche une notification d'avertissement
   */
  warning(message: string, action: string = 'OK', config?: NotificationConfig): MatSnackBarRef<any> {
    return this.show(message, action, {
      ...this.defaultConfig,
      ...config,
      type: NotificationType.WARNING,
      panelClass: ['notification-warning', 'custom-snackbar']
    });
  }

  /**
   * Affiche une notification d'information
   */
  info(message: string, action: string = 'OK', config?: NotificationConfig): MatSnackBarRef<any> {
    return this.show(message, action, {
      ...this.defaultConfig,
      ...config,
      type: NotificationType.INFO,
      panelClass: ['notification-info', 'custom-snackbar']
    });
  }

  /**
   * Affiche une notification personnalisée
   */
  show(message: string, action?: string, config?: NotificationConfig): MatSnackBarRef<any> {
    const finalConfig = { ...this.defaultConfig, ...config };

    // Ajoute l'icône au message si nécessaire
    let finalMessage = message;
    if (finalConfig.showIcon && finalConfig.type) {
      const icon = this.getIcon(finalConfig.type);
      finalMessage = `${icon} ${message}`;
    }

    return this.snackBar.open(finalMessage, action, finalConfig);
  }

  /**
   * Affiche une notification de chargement
   */
  loading(message: string = 'Chargement en cours...'): MatSnackBarRef<any> {
    return this.snackBar.open(` ${message}`, '', {
      horizontalPosition: 'center',
      verticalPosition: 'bottom',
      panelClass: ['notification-loading', 'custom-snackbar']
    });
  }

  /**
   * Ferme toutes les notifications
   */
  dismiss(): void {
    this.snackBar.dismiss();
  }

  /**
   * Affiche une boîte de dialogue de confirmation
   */
  confirm(
    title: string,
    message: string,
    confirmText: string = 'Confirmer',
    cancelText: string = 'Annuler'
  ): Promise<boolean> {
    return new Promise((resolve) => {
      const dialogRef = this.dialog.open(ConfirmDialogComponent, {
        width: '400px',
        data: { title, message, confirmText, cancelText },
        panelClass: 'custom-dialog'
      });

      dialogRef.afterClosed().subscribe(result => {
        resolve(result === true);
      });
    });
  }

  /**
   * Affiche une notification avec action personnalisée
   */
  showWithAction(
    message: string,
    actionText: string,
    actionCallback: () => void,
    config?: NotificationConfig
  ): void {
    const snackBarRef = this.show(message, actionText, {
      ...config,
      duration: 8000
    });

    snackBarRef.onAction().subscribe(() => {
      actionCallback();
    });
  }

  /**
   * Affiche une notification de succès avec redirection
   */
  successWithRedirect(
    message: string,
    redirectUrl: string,
    redirectText: string = 'Voir',
    router?: any
  ): void {
    this.showWithAction(message, redirectText, () => {
      if (router) {
        router.navigate([redirectUrl]);
      }
    }, { type: NotificationType.SUCCESS });
  }

  /**
   * Retourne l'icône correspondant au type de notification
   */
  private getIcon(type: NotificationType): string {
    const icons = {
      [NotificationType.SUCCESS]: '✓',
      [NotificationType.ERROR]: '✗',
      [NotificationType.WARNING]: '⚠',
      [NotificationType.INFO]: 'ℹ'
    };
    return icons[type] || '';
  }
}

// Composant de dialogue de confirmation
import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import {MatButton} from '@angular/material/button';

export interface ConfirmDialogData {
  title: string;
  message: string;
  confirmText: string;
  cancelText: string;
}

@Component({
  selector: 'app-confirm-dialog',
  template: `
    <h2 mat-dialog-title>{{ data.title }}</h2>
    <mat-dialog-content>
      <p>{{ data.message }}</p>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button (click)="onCancel()">{{ data.cancelText }}</button>
      <button mat-raised-button color="primary" (click)="onConfirm()">
        {{ data.confirmText }}
      </button>
    </mat-dialog-actions>
  `,
  imports: [
    MatDialogContent,
    MatDialogActions,
    MatDialogTitle,
    MatButton
  ],
  styles: [`
    mat-dialog-content {
      padding: 20px 0;
    }

    p {
      margin: 0;
      color: #666;
    }
  `]
})
export class ConfirmDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<ConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ConfirmDialogData
  ) {}

  onConfirm(): void {
    this.dialogRef.close(true);
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }
}
