import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CodeGroupsComponent } from './list/code-groups.component';
import { CodeGroupsDetailComponent } from './detail/code-groups-detail.component';
import { CodeGroupsUpdateComponent } from './update/code-groups-update.component';
import { CodeGroupsDeleteDialogComponent } from './delete/code-groups-delete-dialog.component';
import { CodeGroupsRoutingModule } from './route/code-groups-routing.module';

@NgModule({
  imports: [SharedModule, CodeGroupsRoutingModule],
  declarations: [CodeGroupsComponent, CodeGroupsDetailComponent, CodeGroupsUpdateComponent, CodeGroupsDeleteDialogComponent],
  entryComponents: [CodeGroupsDeleteDialogComponent],
})
export class CodeGroupsModule {}
