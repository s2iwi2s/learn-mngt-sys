/* eslint-disable no-console */
import { Component, OnInit } from '@angular/core'
import { HttpResponse } from '@angular/common/http'
import { FormBuilder } from '@angular/forms'
import { ActivatedRoute } from '@angular/router'
import { Observable, ReplaySubject } from 'rxjs'
import { finalize } from 'rxjs/operators'

import { IImageStore, ImageStore } from '../image-store.model'
import { ImageStoreService } from '../service/image-store.service'

@Component({
  selector: 'jhi-image-store-update',
  templateUrl: './image-store-update.component.html'
})
export class ImageStoreUpdateComponent implements OnInit {
  isSaving = false

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    base64: []
  })

  constructor (protected imageStoreService: ImageStoreService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit (): void {
    this.activatedRoute.data.subscribe(({ imageStore }) => {
      this.updateForm(imageStore)
    })
  }
  onFileSelected (event: any): void {
    console.log('event.target.files[0]:', event.target.files[0])
    const fileName: string = event.target.files[0].name
    console.log(`fileName=${fileName}`);
    this.convertFile(event.target.files[0]).subscribe(base64Value => {
      console.log('values', {
        base64: base64Value,
        name: fileName
      })
      this.editForm.patchValue({
        base64: base64Value,
        name: event.target.files[0].name
      })
    })
  }
  convertFile (file: File): Observable<string> {
    const result = new ReplaySubject<string>(1)
    const reader = new FileReader()
    reader.readAsBinaryString(file)
    reader.onload = event => result.next(btoa(event.target!.result!.toString()))
    return result
  }
  previousState (): void {
    window.history.back()
  }

  save (): void {
    this.isSaving = true
    const imageStore = this.createFromForm()
    if (imageStore.id !== undefined) {
      this.subscribeToSaveResponse(this.imageStoreService.update(imageStore))
    } else {
      this.subscribeToSaveResponse(this.imageStoreService.create(imageStore))
    }
  }

  protected subscribeToSaveResponse (result: Observable<HttpResponse<IImageStore>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    )
  }

  protected onSaveSuccess (): void {
    this.previousState()
  }

  protected onSaveError (): void {
    // Api for inheritance.
  }

  protected onSaveFinalize (): void {
    this.isSaving = false
  }

  protected updateForm (imageStore: IImageStore): void {
    this.editForm.patchValue({
      id: imageStore.id,
      name: imageStore.name,
      description: imageStore.description,
      base64: imageStore.base64
    })
  }

  protected createFromForm (): IImageStore {
    return {
      ...new ImageStore(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      base64: this.editForm.get(['base64'])!.value
    }
  }
}
