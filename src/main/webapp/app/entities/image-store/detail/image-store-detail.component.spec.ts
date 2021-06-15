import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ImageStoreDetailComponent } from './image-store-detail.component';

describe('Component Tests', () => {
  describe('ImageStore Management Detail Component', () => {
    let comp: ImageStoreDetailComponent;
    let fixture: ComponentFixture<ImageStoreDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ImageStoreDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ imageStore: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ImageStoreDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ImageStoreDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load imageStore on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.imageStore).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
