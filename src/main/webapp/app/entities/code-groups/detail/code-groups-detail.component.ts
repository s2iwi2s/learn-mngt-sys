import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICodeGroups } from '../code-groups.model';

@Component({
  selector: 'jhi-code-groups-detail',
  templateUrl: './code-groups-detail.component.html',
})
export class CodeGroupsDetailComponent implements OnInit {
  codeGroups: ICodeGroups | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ codeGroups }) => {
      this.codeGroups = codeGroups;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
