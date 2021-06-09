import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICodeGroups, CodeGroups } from '../code-groups.model';
import { CodeGroupsService } from '../service/code-groups.service';

@Injectable({ providedIn: 'root' })
export class CodeGroupsRoutingResolveService implements Resolve<ICodeGroups> {
  constructor(protected service: CodeGroupsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICodeGroups> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((codeGroups: HttpResponse<CodeGroups>) => {
          if (codeGroups.body) {
            return of(codeGroups.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CodeGroups());
  }
}
