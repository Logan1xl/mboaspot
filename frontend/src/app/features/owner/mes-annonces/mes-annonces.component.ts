// src/app/features/owner/mes-annonces/mes-annonces.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { MapComponent } from '../../../shared/components/map/map.component';
import { AnnoncesService } from '../../../core/services/annonces.service';
import { UploadService } from '../../../core/services/upload.service';
import { AuthService } from '../../../core/auth/auth.service';
import { Annonce } from '../../../core/models';
import { ToastrService } from 'ngx-toastr';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-mes-annonces',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, NavbarComponent,MapComponent],
  templateUrl: './mes-annonces.component.html',
  styleUrls: ['./mes-annonces.component.scss']
})
export class MesAnnoncesComponent implements OnInit {
  annonces: Annonce[] = [];
  isLoading = true;
  showModal = false;
  isEditMode = false;
  annonceForm: FormGroup;
  isSubmitting = false;
  selectedFiles: File[] = [];
  uploadedImageUrls: string[] = [];
  currentAnnonceId: number | null = null;

  types = ['Hôtel', 'Appartement', 'Studio', 'Villa', 'Chambre'];
  villes: string[] = [];

  constructor(
    private fb: FormBuilder,
    private annoncesService: AnnoncesService,
    private uploadService: UploadService,
    private authService: AuthService,
    private toastr: ToastrService
  ) {
    this.annonceForm = this.fb.group({
      titre: ['', [Validators.required, Validators.minLength(5)]],
      description: ['', [Validators.required, Validators.minLength(20)]],
      prix: [null, [Validators.required, Validators.min(1000)]],
      adresse: ['', Validators.required],
      ville: ['', Validators.required],
      typeAnnonce: ['Appartement', Validators.required],
      nbreChambres: [1, [Validators.required, Validators.min(1)]],
      nbreLits: [1, [Validators.required, Validators.min(1)]],
      maxInvites: [2, [Validators.required, Validators.min(1)]],
      latitude: [null],
      longitude: [null]
    });
  }

  ngOnInit(): void {
    this.loadAnnonces();
    this.loadVilles();
  }

  loadAnnonces(): void {
    const userId = this.authService.getCurrentUserId();
    if (!userId) return;

    this.annoncesService.getAnnoncesProprietaire(userId).subscribe({
      next: (annonces) => {
        this.annonces = annonces;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
        this.toastr.error('Erreur lors du chargement', 'Erreur');
      }
    });
  }

  loadVilles(): void {
    this.annoncesService.getVillesDisponibles().subscribe({
      next: (villes) => {
        this.villes = villes.length > 0 ? villes : ['Douala', 'Yaoundé', 'Bafoussam'];
      }
    });
  }

  openCreateModal(): void {
    this.isEditMode = false;
    this.currentAnnonceId = null;
    this.annonceForm.reset({
      typeAnnonce: 'Appartement',
      nbreChambres: 1,
      nbreLits: 1,
      maxInvites: 2
    });
    this.selectedFiles = [];
    this.uploadedImageUrls = [];
    this.showModal = true;
  }

  openEditModal(annonce: Annonce): void {
    this.isEditMode = true;
    this.currentAnnonceId = annonce.id;
    this.annonceForm.patchValue(annonce);
    this.uploadedImageUrls = annonce.urlImages || [];
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.annonceForm.reset();
    this.selectedFiles = [];
    this.uploadedImageUrls = [];
  }

  onFileSelect(event: any): void {
    const files = Array.from(event.target.files) as File[];
    this.selectedFiles = [...this.selectedFiles, ...files].slice(0, 5);
  }

  removeFile(index: number): void {
    this.selectedFiles.splice(index, 1);
  }

  async uploadImages(): Promise<string[]> {
    if (this.selectedFiles.length === 0) {
      return this.uploadedImageUrls;
    }

    const uploadPromises = this.selectedFiles.map(file =>
      this.uploadService.uploadImage(file).toPromise()
    );

    try {
      const results = await Promise.all(uploadPromises);
      const urls = results.map(r => r?.url || '').filter(Boolean);
      return [...this.uploadedImageUrls, ...urls];
    } catch (error) {
      this.toastr.error('Erreur lors de l\'upload des images', 'Erreur');
      return this.uploadedImageUrls;
    }
  }

 async onSubmit(): Promise<void> {
  if (this.annonceForm.invalid) {
    Object.keys(this.annonceForm.controls).forEach(key => {
      this.annonceForm.get(key)?.markAsTouched();
    });
    this.toastr.error('Veuillez remplir tous les champs requis', 'Formulaire invalide');
    return;
  }

  this.isSubmitting = true;

  try {
    // Upload des images
    const imageUrls = await this.uploadImages();

    if (imageUrls.length === 0 && !this.isEditMode) {
      this.toastr.warning('Veuillez ajouter au moins une photo', 'Photos manquantes');
      this.isSubmitting = false;
      return;
    }

    const userId = this.authService.getCurrentUserId();
    if (!userId) {
      this.toastr.error('Session expirée, veuillez vous reconnecter', 'Erreur');
      this.isSubmitting = false;
      return;
    }

    // Préparer les données de l'annonce
    const formValue = this.annonceForm.value;

    const annonceData = {
      titre: formValue.titre,
      description: formValue.description,
      prix: formValue.prix,
      adresse: formValue.adresse,
      ville: formValue.ville,
      quartier: formValue.ville, // Utiliser la ville comme quartier par défaut
      typeAnnonce: formValue.typeAnnonce,
      nbreChambres: formValue.nbreChambres,
      nbreLits: formValue.nbreLits,
      maxInvites: formValue.maxInvites,
      latitude: formValue.latitude || 4.0511, // Coordonnées par défaut de Douala
      longitude: formValue.longitude || 9.7679,
      urlImages: imageUrls,
      urlImagePrincipale: imageUrls[0] || '',
      idProprietaire: userId, // ID de l'utilisateur connecté
      estActive: true
    };

    console.log('Données envoyées au backend:', annonceData);

    if (this.isEditMode && this.currentAnnonceId) {
      // Mode modification
      this.annoncesService.updateAnnonce(this.currentAnnonceId, annonceData).subscribe({
        next: (response) => {
          console.log('Réponse modification:', response);
          this.toastr.success('Annonce modifiée avec succès', 'Succès');
          this.loadAnnonces();
          this.closeModal();
          this.isSubmitting = false;
        },
        error: (error) => {
          console.error('Erreur modification:', error);
          this.isSubmitting = false;
          this.toastr.error(
            error.error?.message || 'Erreur lors de la modification',
            'Erreur'
          );
        }
      });
    } else {
      // Mode création
      this.annoncesService.createAnnonce(annonceData).subscribe({
        next: (response) => {
          console.log('Réponse création:', response);
          this.toastr.success('Annonce créée avec succès', 'Succès');
          this.loadAnnonces();
          this.closeModal();
          this.isSubmitting = false;
        },
        error: (error) => {
          console.error('Erreur création:', error);
          this.isSubmitting = false;
          this.toastr.error(
            error.error?.message || 'Erreur lors de la création',
            'Erreur'
          );
        }
      });
    }
  } catch (error) {
    console.error('Erreur traitement:', error);
    this.isSubmitting = false;
    this.toastr.error('Erreur lors du traitement', 'Erreur');
  }
}
  toggleActivation(annonce: Annonce): void {
    this.annoncesService.activerAnnonce(annonce.id, !annonce.estActive).subscribe({
      next: () => {
        const status = !annonce.estActive ? 'activée' : 'désactivée';
        this.toastr.success(`Annonce ${status}`, 'Succès');
        this.loadAnnonces();
      },
      error: () => {
        this.toastr.error('Erreur lors de la modification', 'Erreur');
      }
    });
  }

  deleteAnnonce(annonce: Annonce): void {
    if (!confirm(`Êtes-vous sûr de vouloir supprimer "${annonce.titre}" ?`)) {
      return;
    }

    this.annoncesService.deleteAnnonce(annonce.id).subscribe({
      next: () => {
        this.toastr.success('Annonce supprimée', 'Succès');
        this.loadAnnonces();
      },
      error: () => {
        this.toastr.error('Erreur lors de la suppression', 'Erreur');
      }
    });
  }

  getImageUrl(url: string): string {
    return url.startsWith('http') ? url : `${environment.apiUrl}${url}`;
  }

onMapLocationSelected(event: { lat: number; lng: number }): void {
  console.log('Position sélectionnée:', event);
  this.annonceForm.patchValue({
    latitude: event.lat,
    longitude: event.lng
  });
}
}
