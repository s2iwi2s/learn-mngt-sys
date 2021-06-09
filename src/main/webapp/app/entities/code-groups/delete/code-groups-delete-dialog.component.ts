import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICodeGroups } from '../code-groups.model';
import { CodeGroupsService } from '../service/code-groups.service';

@Component({
  templateUrl: './code-groups-delete-dialog.component.html',
})
export class CodeGroupsDeleteDialogComponent {
  codeGroups?: ICodeGroups;

  constructor(protected codeGroupsService: CodeGroupsService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.codeGroupsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
