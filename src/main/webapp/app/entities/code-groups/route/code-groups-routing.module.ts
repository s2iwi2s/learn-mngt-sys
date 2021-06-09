import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CodeGroupsComponent } from '../list/code-groups.component';
import { CodeGroupsDetailComponent } from '../detail/code-groups-detail.component';
import { CodeGroupsUpdateComponent } from '../update/code-groups-update.component';
import { CodeGroupsRoutingResolveService } from './code-groups-routing-resolve.service';

const codeGroupsRoute: Routes = [
  {
    path: '',
    component: CodeGroupsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CodeGroupsDetailComponent,
    resolve: {
      codeGroups: CodeGroupsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CodeGroupsUpdateComponent,
    resolve: {
      codeGroups: CodeGroupsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CodeGroupsUpdateComponent,
    resolve: {
      codeGroups: CodeGroupsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(codeGroupsRoute)],
  exports: [RouterModule],
})
export class CodeGroupsRoutingModule {}
