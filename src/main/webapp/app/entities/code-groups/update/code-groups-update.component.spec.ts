jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CodeGroupsService } from '../service/code-groups.service';
import { ICodeGroups, CodeGroups } from '../code-groups.model';

import { CodeGroupsUpdateComponent } from './code-groups-update.component';

describe('Component Tests', () => {
  describe('CodeGroups Management Update Component', () => {
    let comp: CodeGroupsUpdateComponent;
    let fixture: ComponentFixture<CodeGroupsUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let codeGroupsService: CodeGroupsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CodeGroupsUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CodeGroupsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CodeGroupsUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      codeGroupsService = TestBed.inject(CodeGroupsService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const codeGroups: ICodeGroups = { id: 456 };

        activatedRoute.data = of({ codeGroups });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(codeGroups));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const codeGroups = { id: 123 };
        spyOn(codeGroupsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ codeGroups });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: codeGroups }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(codeGroupsService.update).toHaveBeenCalledWith(codeGroups);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const codeGroups = new CodeGroups();
        spyOn(codeGroupsService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ codeGroups });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: codeGroups }));
        saveSubject.complete();

        // THEN
        expect(codeGroupsService.create).toHaveBeenCalledWith(codeGroups);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const codeGroups = { id: 123 };
        spyOn(codeGroupsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ codeGroups });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(codeGroupsService.update).toHaveBeenCalledWith(codeGroups);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
