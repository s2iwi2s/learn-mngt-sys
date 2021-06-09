import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICodeGroups, CodeGroups } from '../code-groups.model';

import { CodeGroupsService } from './code-groups.service';

describe('Service Tests', () => {
  describe('CodeGroups Service', () => {
    let service: CodeGroupsService;
    let httpMock: HttpTestingController;
    let elemDefault: ICodeGroups;
    let expectedResult: ICodeGroups | ICodeGroups[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CodeGroupsService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        code: 'AAAAAAA',
        value: 'AAAAAAA',
        description: 'AAAAAAA',
        json: 'AAAAAAA',
        priority: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a CodeGroups', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new CodeGroups()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CodeGroups', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            code: 'BBBBBB',
            value: 'BBBBBB',
            description: 'BBBBBB',
            json: 'BBBBBB',
            priority: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a CodeGroups', () => {
        const patchObject = Object.assign(
          {
            description: 'BBBBBB',
            json: 'BBBBBB',
            priority: 1,
          },
          new CodeGroups()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CodeGroups', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            code: 'BBBBBB',
            value: 'BBBBBB',
            description: 'BBBBBB',
            json: 'BBBBBB',
            priority: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a CodeGroups', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCodeGroupsToCollectionIfMissing', () => {
        it('should add a CodeGroups to an empty array', () => {
          const codeGroups: ICodeGroups = { id: 123 };
          expectedResult = service.addCodeGroupsToCollectionIfMissing([], codeGroups);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(codeGroups);
        });

        it('should not add a CodeGroups to an array that contains it', () => {
          const codeGroups: ICodeGroups = { id: 123 };
          const codeGroupsCollection: ICodeGroups[] = [
            {
              ...codeGroups,
            },
            { id: 456 },
          ];
          expectedResult = service.addCodeGroupsToCollectionIfMissing(codeGroupsCollection, codeGroups);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CodeGroups to an array that doesn't contain it", () => {
          const codeGroups: ICodeGroups = { id: 123 };
          const codeGroupsCollection: ICodeGroups[] = [{ id: 456 }];
          expectedResult = service.addCodeGroupsToCollectionIfMissing(codeGroupsCollection, codeGroups);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(codeGroups);
        });

        it('should add only unique CodeGroups to an array', () => {
          const codeGroupsArray: ICodeGroups[] = [{ id: 123 }, { id: 456 }, { id: 79601 }];
          const codeGroupsCollection: ICodeGroups[] = [{ id: 123 }];
          expectedResult = service.addCodeGroupsToCollectionIfMissing(codeGroupsCollection, ...codeGroupsArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const codeGroups: ICodeGroups = { id: 123 };
          const codeGroups2: ICodeGroups = { id: 456 };
          expectedResult = service.addCodeGroupsToCollectionIfMissing([], codeGroups, codeGroups2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(codeGroups);
          expect(expectedResult).toContain(codeGroups2);
        });

        it('should accept null and undefined values', () => {
          const codeGroups: ICodeGroups = { id: 123 };
          expectedResult = service.addCodeGroupsToCollectionIfMissing([], null, codeGroups, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(codeGroups);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
