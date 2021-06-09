jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IStudent, Student } from '../student.model';
import { StudentService } from '../service/student.service';

import { StudentRoutingResolveService } from './student-routing-resolve.service';

describe('Service Tests', () => {
  describe('Student routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: StudentRoutingResolveService;
    let service: StudentService;
    let resultStudent: IStudent | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(StudentRoutingResolveService);
      service = TestBed.inject(StudentService);
      resultStudent = undefined;
    });

    describe('resolve', () => {
      it('should return IStudent returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStudent = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultStudent).toEqual({ id: 123 });
      });

      it('should return new IStudent if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStudent = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultStudent).toEqual(new Student());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStudent = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultStudent).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
