jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { StudentService } from '../service/student.service';
import { IStudent, Student } from '../student.model';
import { ICodeGroups } from 'app/entities/code-groups/code-groups.model';
import { CodeGroupsService } from 'app/entities/code-groups/service/code-groups.service';

import { StudentUpdateComponent } from './student-update.component';

describe('Component Tests', () => {
  describe('Student Management Update Component', () => {
    let comp: StudentUpdateComponent;
    let fixture: ComponentFixture<StudentUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let studentService: StudentService;
    let codeGroupsService: CodeGroupsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [StudentUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(StudentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StudentUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      studentService = TestBed.inject(StudentService);
      codeGroupsService = TestBed.inject(CodeGroupsService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call gender query and add missing value', () => {
        const student: IStudent = { id: 456 };
        const gender: ICodeGroups = { id: 13248 };
        student.gender = gender;

        const genderCollection: ICodeGroups[] = [{ id: 89658 }];
        spyOn(codeGroupsService, 'query').and.returnValue(of(new HttpResponse({ body: genderCollection })));
        const expectedCollection: ICodeGroups[] = [gender, ...genderCollection];
        spyOn(codeGroupsService, 'addCodeGroupsToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ student });
        comp.ngOnInit();

        expect(codeGroupsService.query).toHaveBeenCalled();
        expect(codeGroupsService.addCodeGroupsToCollectionIfMissing).toHaveBeenCalledWith(genderCollection, gender);
        expect(comp.gendersCollection).toEqual(expectedCollection);
      });

      it('Should call parentCivilStatus query and add missing value', () => {
        const student: IStudent = { id: 456 };
        const parentCivilStatus: ICodeGroups = { id: 96952 };
        student.parentCivilStatus = parentCivilStatus;

        const parentCivilStatusCollection: ICodeGroups[] = [{ id: 86508 }];
        spyOn(codeGroupsService, 'query').and.returnValue(of(new HttpResponse({ body: parentCivilStatusCollection })));
        const expectedCollection: ICodeGroups[] = [parentCivilStatus, ...parentCivilStatusCollection];
        spyOn(codeGroupsService, 'addCodeGroupsToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ student });
        comp.ngOnInit();

        expect(codeGroupsService.query).toHaveBeenCalled();
        expect(codeGroupsService.addCodeGroupsToCollectionIfMissing).toHaveBeenCalledWith(parentCivilStatusCollection, parentCivilStatus);
        expect(comp.parentCivilStatusesCollection).toEqual(expectedCollection);
      });

      it('Should call course query and add missing value', () => {
        const student: IStudent = { id: 456 };
        const course: ICodeGroups = { id: 3150 };
        student.course = course;

        const courseCollection: ICodeGroups[] = [{ id: 39759 }];
        spyOn(codeGroupsService, 'query').and.returnValue(of(new HttpResponse({ body: courseCollection })));
        const expectedCollection: ICodeGroups[] = [course, ...courseCollection];
        spyOn(codeGroupsService, 'addCodeGroupsToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ student });
        comp.ngOnInit();

        expect(codeGroupsService.query).toHaveBeenCalled();
        expect(codeGroupsService.addCodeGroupsToCollectionIfMissing).toHaveBeenCalledWith(courseCollection, course);
        expect(comp.coursesCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const student: IStudent = { id: 456 };
        const gender: ICodeGroups = { id: 71446 };
        student.gender = gender;
        const parentCivilStatus: ICodeGroups = { id: 8828 };
        student.parentCivilStatus = parentCivilStatus;
        const course: ICodeGroups = { id: 78009 };
        student.course = course;

        activatedRoute.data = of({ student });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(student));
        expect(comp.gendersCollection).toContain(gender);
        expect(comp.parentCivilStatusesCollection).toContain(parentCivilStatus);
        expect(comp.coursesCollection).toContain(course);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const student = { id: 123 };
        spyOn(studentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ student });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: student }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(studentService.update).toHaveBeenCalledWith(student);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const student = new Student();
        spyOn(studentService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ student });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: student }));
        saveSubject.complete();

        // THEN
        expect(studentService.create).toHaveBeenCalledWith(student);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const student = { id: 123 };
        spyOn(studentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ student });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(studentService.update).toHaveBeenCalledWith(student);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCodeGroupsById', () => {
        it('Should return tracked CodeGroups primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCodeGroupsById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
