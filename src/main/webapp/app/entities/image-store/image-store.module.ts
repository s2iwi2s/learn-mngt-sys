import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ImageStoreComponent } from './list/image-store.component';
import { ImageStoreDetailComponent } from './detail/image-store-detail.component';
import { ImageStoreUpdateComponent } from './update/image-store-update.component';
import { ImageStoreDeleteDialogComponent } from './delete/image-store-delete-dialog.component';
import { ImageStoreRoutingModule } from './route/image-store-routing.module';

@NgModule({
  imports: [SharedModule, ImageStoreRoutingModule],
  declarations: [ImageStoreComponent, ImageStoreDetailComponent, ImageStoreUpdateComponent, ImageStoreDeleteDialogComponent],
  entryComponents: [ImageStoreDeleteDialogComponent],
})
export class ImageStoreModule {}
