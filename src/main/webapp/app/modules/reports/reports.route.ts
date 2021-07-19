import { Routes } from '@angular/router';
import { Authority } from 'app/config/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StudentReportComponent } from './student-report/student-report.component';


export const reportsRoute: Routes = [
  {
    path: 'student-report',
    component: StudentReportComponent,
    data: {
      authorities: [Authority.USER]
    },
    canActivate: [UserRouteAccessService],
  },
]
