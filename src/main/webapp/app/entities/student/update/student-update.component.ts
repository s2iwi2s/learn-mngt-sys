import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IStudent, Student } from '../student.model';
import { StudentService } from '../service/student.service';
import { ICodeGroups } from 'app/entities/code-groups/code-groups.model';
import { CodeGroupsService } from 'app/entities/code-groups/service/code-groups.service';

@Component({
  selector: 'jhi-student-update',
  templateUrl: './student-update.component.html',
})
export class StudentUpdateComponent implements OnInit {
  isSaving = false;

  gendersCollection: ICodeGroups[] = [];
  parentCivilStatusesCollection: ICodeGroups[] = [];
  coursesCollection: ICodeGroups[] = [];

  editForm = this.fb.group({
    id: [],
    lrn: [],
    firstName: [],
    lastName: [],
    birthDate: [],
    birthPlace: [],
    contactNo: [],
    address1: [],
    address2: [],
    city: [],
    zipCode: [],
    country: [],
    fathersName: [],
    fathersOccupation: [],
    mothersName: [],
    mothersOccupation: [],
    guardianName: [],
    gender: [],
    parentCivilStatus: [],
    course: [],
  });

  constructor(
    protected studentService: StudentService,
    protected codeGroupsService: CodeGroupsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ student }) => {
      if (student.id === undefined) {
        const today = dayjs().startOf('day');
        student.birthDate = today;
      }

      this.updateForm(student);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const student = this.createFromForm();
    if (student.id !== undefined) {
      this.subscribeToSaveResponse(this.studentService.update(student));
    } else {
      this.subscribeToSaveResponse(this.studentService.create(student));
    }
  }

  trackCodeGroupsById(index: number, item: ICodeGroups): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudent>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(student: IStudent): void {
    this.editForm.patchValue({
      id: student.id,
      lrn: student.lrn,
      firstName: student.firstName,
      lastName: student.lastName,
      birthDate: student.birthDate ? student.birthDate.format(DATE_TIME_FORMAT) : null,
      birthPlace: student.birthPlace,
      contactNo: student.contactNo,
      address1: student.address1,
      address2: student.address2,
      city: student.city,
      zipCode: student.zipCode,
      country: student.country,
      fathersName: student.fathersName,
      fathersOccupation: student.fathersOccupation,
      mothersName: student.mothersName,
      mothersOccupation: student.mothersOccupation,
      guardianName: student.guardianName,
      gender: student.gender,
      parentCivilStatus: student.parentCivilStatus,
      course: student.course,
    });

    this.gendersCollection = this.codeGroupsService.addCodeGroupsToCollectionIfMissing(this.gendersCollection, student.gender);
    this.parentCivilStatusesCollection = this.codeGroupsService.addCodeGroupsToCollectionIfMissing(
      this.parentCivilStatusesCollection,
      student.parentCivilStatus
    );
    this.coursesCollection = this.codeGroupsService.addCodeGroupsToCollectionIfMissing(this.coursesCollection, student.course);
  }

  protected loadRelationshipsOptions(): void {
    this.codeGroupsService
      .query({ filter: 'student-is-null' })
      .pipe(map((res: HttpResponse<ICodeGroups[]>) => res.body ?? []))
      .pipe(
        map((codeGroups: ICodeGroups[]) =>
          this.codeGroupsService.addCodeGroupsToCollectionIfMissing(codeGroups, this.editForm.get('gender')!.value)
        )
      )
      .subscribe((codeGroups: ICodeGroups[]) => (this.gendersCollection = codeGroups));

    this.codeGroupsService
      .query({ filter: 'student-is-null' })
      .pipe(map((res: HttpResponse<ICodeGroups[]>) => res.body ?? []))
      .pipe(
        map((codeGroups: ICodeGroups[]) =>
          this.codeGroupsService.addCodeGroupsToCollectionIfMissing(codeGroups, this.editForm.get('parentCivilStatus')!.value)
        )
      )
      .subscribe((codeGroups: ICodeGroups[]) => (this.parentCivilStatusesCollection = codeGroups));

    this.codeGroupsService
      .query({ filter: 'student-is-null' })
      .pipe(map((res: HttpResponse<ICodeGroups[]>) => res.body ?? []))
      .pipe(
        map((codeGroups: ICodeGroups[]) =>
          this.codeGroupsService.addCodeGroupsToCollectionIfMissing(codeGroups, this.editForm.get('course')!.value)
        )
      )
      .subscribe((codeGroups: ICodeGroups[]) => (this.coursesCollection = codeGroups));
  }

  protected createFromForm(): IStudent {
    return {
      ...new Student(),
      id: this.editForm.get(['id'])!.value,
      lrn: this.editForm.get(['lrn'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      birthDate: this.editForm.get(['birthDate'])!.value ? dayjs(this.editForm.get(['birthDate'])!.value, DATE_TIME_FORMAT) : undefined,
      birthPlace: this.editForm.get(['birthPlace'])!.value,
      contactNo: this.editForm.get(['contactNo'])!.value,
      address1: this.editForm.get(['address1'])!.value,
      address2: this.editForm.get(['address2'])!.value,
      city: this.editForm.get(['city'])!.value,
      zipCode: this.editForm.get(['zipCode'])!.value,
      country: this.editForm.get(['country'])!.value,
      fathersName: this.editForm.get(['fathersName'])!.value,
      fathersOccupation: this.editForm.get(['fathersOccupation'])!.value,
      mothersName: this.editForm.get(['mothersName'])!.value,
      mothersOccupation: this.editForm.get(['mothersOccupation'])!.value,
      guardianName: this.editForm.get(['guardianName'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      parentCivilStatus: this.editForm.get(['parentCivilStatus'])!.value,
      course: this.editForm.get(['course'])!.value,
    };
  }
}
