// src/app/features/auth/register/register.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/auth/auth.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  registerForm: FormGroup;
  isLoading = false;
  showPassword = false;
  showConfirmPassword = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private toastr: ToastrService
  ) {
    this.registerForm = this.fb.group({
      nom: ['', [Validators.required, Validators.minLength(2)]],
      prenom: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      numeroTelephone: ['', [Validators.required, Validators.pattern(/^[0-9]{9}$/)]],
      motDePasse: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]],
      role: ['VOYAGEUR', [Validators.required]],
      // Champs propriétaire
      nomEntreprise: [''],
      numeroIdentification: [''],
      compteBancaire: ['']
    }, { validators: this.passwordMatchValidator });

    // Gérer les champs conditionnels
    this.registerForm.get('role')?.valueChanges.subscribe(role => {
      this.updateConditionalValidators(role);
    });
  }

  passwordMatchValidator(form: FormGroup) {
    const password = form.get('motDePasse')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordMismatch: true };
  }

  updateConditionalValidators(role: string) {
    const nomEntreprise = this.registerForm.get('nomEntreprise');
    const numeroIdentification = this.registerForm.get('numeroIdentification');

    if (role === 'PROPRIETAIRE') {
      nomEntreprise?.setValidators([Validators.required]);
      numeroIdentification?.setValidators([Validators.required]);
    } else {
      nomEntreprise?.clearValidators();
      numeroIdentification?.clearValidators();
    }

    nomEntreprise?.updateValueAndValidity();
    numeroIdentification?.updateValueAndValidity();
  }

  get f() { return this.registerForm.controls; }

  togglePasswordVisibility(field: 'password' | 'confirm'): void {
    if (field === 'password') {
      this.showPassword = !this.showPassword;
    } else {
      this.showConfirmPassword = !this.showConfirmPassword;
    }
  }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      Object.keys(this.registerForm.controls).forEach(key => {
        this.registerForm.get(key)?.markAsTouched();
      });
      return;
    }

    this.isLoading = true;
    const formData = { ...this.registerForm.value };
    delete formData.confirmPassword;

    this.authService.register(formData).subscribe({
      next: (response) => {
        this.toastr.success('Compte créé avec succès !', 'Bienvenue');

        if (response.role === 'PROPRIETAIRE') {
          this.router.navigate(['/owner/dashboard']);
        } else {
          this.router.navigate(['/home']);
        }
      },
      error: (error) => {
        this.isLoading = false;
        const message = error.error?.message || 'Erreur lors de l\'inscription';
        this.toastr.error(message, 'Erreur');
      }
    });
  }
}
