import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICodeGroups, CodeGroups } from '../code-groups.model';
import { CodeGroupsService } from '../service/code-groups.service';

@Component({
  selector: 'jhi-code-groups-update',
  templateUrl: './code-groups-update.component.html',
})
export class CodeGroupsUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    code: [],
    value: [],
    description: [],
    json: [],
    priority: [],
  });

  constructor(protected codeGroupsService: CodeGroupsService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ codeGroups }) => {
      this.updateForm(codeGroups);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const codeGroups = this.createFromForm();
    if (codeGroups.id !== undefined) {
      this.subscribeToSaveResponse(this.codeGroupsService.update(codeGroups));
    } else {
      this.subscribeToSaveResponse(this.codeGroupsService.create(codeGroups));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICodeGroups>>): void {
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

  protected updateForm(codeGroups: ICodeGroups): void {
    this.editForm.patchValue({
      id: codeGroups.id,
      code: codeGroups.code,
      value: codeGroups.value,
      description: codeGroups.description,
      json: codeGroups.json,
      priority: codeGroups.priority,
    });
  }

  protected createFromForm(): ICodeGroups {
    return {
      ...new CodeGroups(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      value: this.editForm.get(['value'])!.value,
      description: this.editForm.get(['description'])!.value,
      json: this.editForm.get(['json'])!.value,
      priority: this.editForm.get(['priority'])!.value,
    };
  }
}
