import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HTTP_INTERCEPTORS } from '@angular/common/http';

// Services
import { AuthService } from './auth/auth.service';
import { AuthGuard } from './auth/auth.guard';
import { RoleGuard } from './auth/role.guard';
import { ApiService } from './services/api.service';
import { NotificationService } from './services/notification.service';

// Interceptors
import { JwtInterceptor } from './interceptors/jwt.interceptor';

// Layouts (standalone components)
import { ClientLayoutComponent } from './layouts/client-layout/client-layout.component';
import { OwnerLayoutComponent } from './layouts/owner-layout/owner-layout.component';
import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';

@NgModule({
  imports: [
    CommonModule,
    // Import standalone layout components
    ClientLayoutComponent,
    OwnerLayoutComponent,
    AdminLayoutComponent
  ],
  providers: [
    AuthService,
    AuthGuard,
    RoleGuard,
    ApiService,
    NotificationService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true
    }
  ]
})
export class CoreModule { }
