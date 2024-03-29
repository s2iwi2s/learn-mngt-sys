jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IImageStore, ImageStore } from '../image-store.model';
import { ImageStoreService } from '../service/image-store.service';

import { ImageStoreRoutingResolveService } from './image-store-routing-resolve.service';

describe('Service Tests', () => {
  describe('ImageStore routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ImageStoreRoutingResolveService;
    let service: ImageStoreService;
    let resultImageStore: IImageStore | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ImageStoreRoutingResolveService);
      service = TestBed.inject(ImageStoreService);
      resultImageStore = undefined;
    });

    describe('resolve', () => {
      it('should return IImageStore returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultImageStore = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultImageStore).toEqual({ id: 123 });
      });

      it('should return new IImageStore if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultImageStore = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultImageStore).toEqual(new ImageStore());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultImageStore = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultImageStore).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
