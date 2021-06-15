import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IImageStore } from '../image-store.model';

@Component({
  selector: 'jhi-image-store-detail',
  templateUrl: './image-store-detail.component.html',
})
export class ImageStoreDetailComponent implements OnInit {
  imageStore: IImageStore | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ imageStore }) => {
      this.imageStore = imageStore;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
