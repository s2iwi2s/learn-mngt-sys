import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ImageStoreComponent } from '../list/image-store.component';
import { ImageStoreDetailComponent } from '../detail/image-store-detail.component';
import { ImageStoreUpdateComponent } from '../update/image-store-update.component';
import { ImageStoreRoutingResolveService } from './image-store-routing-resolve.service';

const imageStoreRoute: Routes = [
  {
    path: '',
    component: ImageStoreComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ImageStoreDetailComponent,
    resolve: {
      imageStore: ImageStoreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ImageStoreUpdateComponent,
    resolve: {
      imageStore: ImageStoreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ImageStoreUpdateComponent,
    resolve: {
      imageStore: ImageStoreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(imageStoreRoute)],
  exports: [RouterModule],
})
export class ImageStoreRoutingModule {}
