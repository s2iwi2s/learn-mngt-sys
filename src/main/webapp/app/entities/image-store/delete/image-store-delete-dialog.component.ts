import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IImageStore } from '../image-store.model';
import { ImageStoreService } from '../service/image-store.service';

@Component({
  templateUrl: './image-store-delete-dialog.component.html',
})
export class ImageStoreDeleteDialogComponent {
  imageStore?: IImageStore;

  constructor(protected imageStoreService: ImageStoreService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.imageStoreService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
