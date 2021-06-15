import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IImageStore, ImageStore } from '../image-store.model';

import { ImageStoreService } from './image-store.service';

describe('Service Tests', () => {
  describe('ImageStore Service', () => {
    let service: ImageStoreService;
    let httpMock: HttpTestingController;
    let elemDefault: IImageStore;
    let expectedResult: IImageStore | IImageStore[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ImageStoreService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        description: 'AAAAAAA',
        store: 'AAAAAAA',
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

      it('should create a ImageStore', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ImageStore()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ImageStore', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
            store: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ImageStore', () => {
        const patchObject = Object.assign(
          {
            description: 'BBBBBB',
            store: 'BBBBBB',
          },
          new ImageStore()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ImageStore', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
            store: 'BBBBBB',
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

      it('should delete a ImageStore', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addImageStoreToCollectionIfMissing', () => {
        it('should add a ImageStore to an empty array', () => {
          const imageStore: IImageStore = { id: 123 };
          expectedResult = service.addImageStoreToCollectionIfMissing([], imageStore);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(imageStore);
        });

        it('should not add a ImageStore to an array that contains it', () => {
          const imageStore: IImageStore = { id: 123 };
          const imageStoreCollection: IImageStore[] = [
            {
              ...imageStore,
            },
            { id: 456 },
          ];
          expectedResult = service.addImageStoreToCollectionIfMissing(imageStoreCollection, imageStore);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ImageStore to an array that doesn't contain it", () => {
          const imageStore: IImageStore = { id: 123 };
          const imageStoreCollection: IImageStore[] = [{ id: 456 }];
          expectedResult = service.addImageStoreToCollectionIfMissing(imageStoreCollection, imageStore);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(imageStore);
        });

        it('should add only unique ImageStore to an array', () => {
          const imageStoreArray: IImageStore[] = [{ id: 123 }, { id: 456 }, { id: 52799 }];
          const imageStoreCollection: IImageStore[] = [{ id: 123 }];
          expectedResult = service.addImageStoreToCollectionIfMissing(imageStoreCollection, ...imageStoreArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const imageStore: IImageStore = { id: 123 };
          const imageStore2: IImageStore = { id: 456 };
          expectedResult = service.addImageStoreToCollectionIfMissing([], imageStore, imageStore2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(imageStore);
          expect(expectedResult).toContain(imageStore2);
        });

        it('should accept null and undefined values', () => {
          const imageStore: IImageStore = { id: 123 };
          expectedResult = service.addImageStoreToCollectionIfMissing([], null, imageStore, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(imageStore);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
