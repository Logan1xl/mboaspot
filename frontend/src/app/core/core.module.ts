import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HTTP_INTERCEPTORS } from '@angular/common/http';

// Services
import { AuthService } from './auth/auth.service';
import { ApiService } from './services/api.service';
import { NotificationService } from './services/notification.service';

// Guards standalone
import { authGuard } from './auth/auth.guard';
import { roleGuard } from './auth/role.guard';

// Interceptor standalone
import { jwtInterceptor } from './interceptors/jwt.interceptor';

// Layouts (standalone components)
import { ClientLayoutComponent } from './layouts/client-layout/client-layout.component';
import { OwnerLayoutComponent } from './layouts/owner-layout/owner-layout.component';
import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';

@NgModule({
  imports: [
    CommonModule,
    ClientLayoutComponent,
    OwnerLayoutComponent,
    AdminLayoutComponent
  ],
  providers: [
    AuthService,
    ApiService,
    NotificationService,
    {
      provide: HTTP_INTERCEPTORS,
      useValue: jwtInterceptor, // <-- corrigé
      multi: true
    }
  ]
})
export class CoreModule { }
