import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ImageStoreService } from '../service/image-store.service';

import { ImageStoreComponent } from './image-store.component';

describe('Component Tests', () => {
  describe('ImageStore Management Component', () => {
    let comp: ImageStoreComponent;
    let fixture: ComponentFixture<ImageStoreComponent>;
    let service: ImageStoreService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ImageStoreComponent],
      })
        .overrideTemplate(ImageStoreComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ImageStoreComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ImageStoreService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.imageStores?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
