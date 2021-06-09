jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICodeGroups, CodeGroups } from '../code-groups.model';
import { CodeGroupsService } from '../service/code-groups.service';

import { CodeGroupsRoutingResolveService } from './code-groups-routing-resolve.service';

describe('Service Tests', () => {
  describe('CodeGroups routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CodeGroupsRoutingResolveService;
    let service: CodeGroupsService;
    let resultCodeGroups: ICodeGroups | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CodeGroupsRoutingResolveService);
      service = TestBed.inject(CodeGroupsService);
      resultCodeGroups = undefined;
    });

    describe('resolve', () => {
      it('should return ICodeGroups returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCodeGroups = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCodeGroups).toEqual({ id: 123 });
      });

      it('should return new ICodeGroups if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCodeGroups = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCodeGroups).toEqual(new CodeGroups());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCodeGroups = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCodeGroups).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
