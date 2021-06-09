import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICodeGroups, getCodeGroupsIdentifier } from '../code-groups.model';

export type EntityResponseType = HttpResponse<ICodeGroups>;
export type EntityArrayResponseType = HttpResponse<ICodeGroups[]>;

@Injectable({ providedIn: 'root' })
export class CodeGroupsService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/code-groups');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(codeGroups: ICodeGroups): Observable<EntityResponseType> {
    return this.http.post<ICodeGroups>(this.resourceUrl, codeGroups, { observe: 'response' });
  }

  update(codeGroups: ICodeGroups): Observable<EntityResponseType> {
    return this.http.put<ICodeGroups>(`${this.resourceUrl}/${getCodeGroupsIdentifier(codeGroups) as number}`, codeGroups, {
      observe: 'response',
    });
  }

  partialUpdate(codeGroups: ICodeGroups): Observable<EntityResponseType> {
    return this.http.patch<ICodeGroups>(`${this.resourceUrl}/${getCodeGroupsIdentifier(codeGroups) as number}`, codeGroups, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICodeGroups>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICodeGroups[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCodeGroupsToCollectionIfMissing(
    codeGroupsCollection: ICodeGroups[],
    ...codeGroupsToCheck: (ICodeGroups | null | undefined)[]
  ): ICodeGroups[] {
    const codeGroups: ICodeGroups[] = codeGroupsToCheck.filter(isPresent);
    if (codeGroups.length > 0) {
      const codeGroupsCollectionIdentifiers = codeGroupsCollection.map(codeGroupsItem => getCodeGroupsIdentifier(codeGroupsItem)!);
      const codeGroupsToAdd = codeGroups.filter(codeGroupsItem => {
        const codeGroupsIdentifier = getCodeGroupsIdentifier(codeGroupsItem);
        if (codeGroupsIdentifier == null || codeGroupsCollectionIdentifiers.includes(codeGroupsIdentifier)) {
          return false;
        }
        codeGroupsCollectionIdentifiers.push(codeGroupsIdentifier);
        return true;
      });
      return [...codeGroupsToAdd, ...codeGroupsCollection];
    }
    return codeGroupsCollection;
  }
}
