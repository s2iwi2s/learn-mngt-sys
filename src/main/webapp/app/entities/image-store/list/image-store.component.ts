import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IImageStore } from '../image-store.model';
import { ImageStoreService } from '../service/image-store.service';
import { ImageStoreDeleteDialogComponent } from '../delete/image-store-delete-dialog.component';

@Component({
  selector: 'jhi-image-store',
  templateUrl: './image-store.component.html',
})
export class ImageStoreComponent implements OnInit {
  imageStores?: IImageStore[];
  isLoading = false;

  constructor(protected imageStoreService: ImageStoreService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.imageStoreService.query().subscribe(
      (res: HttpResponse<IImageStore[]>) => {
        this.isLoading = false;
        this.imageStores = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IImageStore): number {
    return item.id!;
  }

  delete(imageStore: IImageStore): void {
    const modalRef = this.modalService.open(ImageStoreDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.imageStore = imageStore;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
