import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IImageStore, ImageStore } from '../image-store.model';
import { ImageStoreService } from '../service/image-store.service';

@Injectable({ providedIn: 'root' })
export class ImageStoreRoutingResolveService implements Resolve<IImageStore> {
  constructor(protected service: ImageStoreService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IImageStore> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((imageStore: HttpResponse<ImageStore>) => {
          if (imageStore.body) {
            return of(imageStore.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ImageStore());
  }
}
