jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ImageStoreService } from '../service/image-store.service';
import { IImageStore, ImageStore } from '../image-store.model';

import { ImageStoreUpdateComponent } from './image-store-update.component';

describe('Component Tests', () => {
  describe('ImageStore Management Update Component', () => {
    let comp: ImageStoreUpdateComponent;
    let fixture: ComponentFixture<ImageStoreUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let imageStoreService: ImageStoreService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ImageStoreUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ImageStoreUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ImageStoreUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      imageStoreService = TestBed.inject(ImageStoreService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const imageStore: IImageStore = { id: 456 };

        activatedRoute.data = of({ imageStore });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(imageStore));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const imageStore = { id: 123 };
        spyOn(imageStoreService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ imageStore });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: imageStore }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(imageStoreService.update).toHaveBeenCalledWith(imageStore);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const imageStore = new ImageStore();
        spyOn(imageStoreService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ imageStore });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: imageStore }));
        saveSubject.complete();

        // THEN
        expect(imageStoreService.create).toHaveBeenCalledWith(imageStore);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const imageStore = { id: 123 };
        spyOn(imageStoreService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ imageStore });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(imageStoreService.update).toHaveBeenCalledWith(imageStore);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
