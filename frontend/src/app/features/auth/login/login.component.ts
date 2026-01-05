// src/app/features/auth/login/login.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../core/auth/auth.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  loginForm: FormGroup;
  isLoading = false;
  showPassword = false;
  returnUrl: string;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private toastr: ToastrService
  ) {
    // Récupérer l'URL de retour
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/home';

    // Créer le formulaire
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      motDePasse: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  get email() {
    return this.loginForm.get('email');
  }

  get motDePasse() {
    return this.loginForm.get('motDePasse');
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      Object.keys(this.loginForm.controls).forEach(key => {
        this.loginForm.get(key)?.markAsTouched();
      });
      return;
    }

    this.isLoading = true;

    this.authService.login(this.loginForm.value).subscribe({
      next: (response) => {
        this.toastr.success('Connexion réussie !', 'Bienvenue');

        // Rediriger selon le rôle
        if (response.role === 'ADMIN') {
          this.router.navigate(['/admin/dashboard']);
        } else if (response.role === 'PROPRIETAIRE') {
          this.router.navigate(['/owner/dashboard']);
        } else {
          this.router.navigate([this.returnUrl]);
        }
      },
      error: (error) => {
        this.isLoading = false;
        const message = error.error?.message || 'Email ou mot de passe incorrect';
        this.toastr.error(message, 'Erreur de connexion');
      }
    });
  }
}

// src/app/features/auth/login/login.component.html
/*
<div class="auth-container">
  <div class="auth-card">
    <div class="auth-header">
      <div class="logo">
        <i class="fas fa-home"></i>
        <h1>MboaSpot</h1>
      </div>
      <h2>Connexion</h2>
      <p>Connectez-vous pour accéder à votre compte</p>
    </div>

    <form [formGroup]="loginForm" (ngSubmit)="onSubmit()" class="auth-form">
      <div class="form-group">
        <label for="email">
          <i class="fas fa-envelope"></i>
          Email
        </label>
        <input
          type="email"
          id="email"
          formControlName="email"
          class="form-control"
          [class.is-invalid]="email?.invalid && email?.touched"
          placeholder="exemple@email.com"
        />
        <div class="invalid-feedback" *ngIf="email?.invalid && email?.touched">
          <span *ngIf="email?.errors?.['required']">L'email est requis</span>
          <span *ngIf="email?.errors?.['email']">Format d'email invalide</span>
        </div>
      </div>

      <div class="form-group">
        <label for="password">
          <i class="fas fa-lock"></i>
          Mot de passe
        </label>
        <div class="password-input">
          <input
            [type]="showPassword ? 'text' : 'password'"
            id="password"
            formControlName="motDePasse"
            class="form-control"
            [class.is-invalid]="motDePasse?.invalid && motDePasse?.touched"
            placeholder="••••••••"
          />
          <button
            type="button"
            class="password-toggle"
            (click)="togglePasswordVisibility()"
          >
            <i [class]="showPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
          </button>
        </div>
        <div class="invalid-feedback" *ngIf="motDePasse?.invalid && motDePasse?.touched">
          <span *ngIf="motDePasse?.errors?.['required']">Le mot de passe est requis</span>
          <span *ngIf="motDePasse?.errors?.['minlength']">Minimum 6 caractères</span>
        </div>
      </div>

      <div class="form-actions">
        <a routerLink="/auth/forgot-password" class="forgot-password">
          Mot de passe oublié ?
        </a>
      </div>

      <button
        type="submit"
        class="btn btn-primary btn-block"
        [disabled]="isLoading"
      >
        <span *ngIf="!isLoading">
          <i class="fas fa-sign-in-alt"></i> Se connecter
        </span>
        <span *ngIf="isLoading">
          <i class="fas fa-spinner fa-spin"></i> Connexion en cours...
        </span>
      </button>
    </form>

    <div class="auth-footer">
      <p>Pas encore de compte ?</p>
      <a routerLink="/auth/register" class="btn btn-ghost">
        <i class="fas fa-user-plus"></i> Créer un compte
      </a>
    </div>
  </div>

  <div class="auth-background">
    <div class="background-overlay"></div>
  </div>
</div>
*/
