import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { reportsRoute } from './reports.route';
import { StudentReportComponent } from './student-report/student-report.component';



@NgModule({
  declarations: [StudentReportComponent],
  imports: [SharedModule, RouterModule.forChild(reportsRoute)],
})
export class ReportsModule { }
