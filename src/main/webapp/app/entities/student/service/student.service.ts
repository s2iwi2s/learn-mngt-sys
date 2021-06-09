import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStudent, getStudentIdentifier } from '../student.model';

export type EntityResponseType = HttpResponse<IStudent>;
export type EntityArrayResponseType = HttpResponse<IStudent[]>;

@Injectable({ providedIn: 'root' })
export class StudentService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/students');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(student: IStudent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(student);
    return this.http
      .post<IStudent>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(student: IStudent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(student);
    return this.http
      .put<IStudent>(`${this.resourceUrl}/${getStudentIdentifier(student) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(student: IStudent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(student);
    return this.http
      .patch<IStudent>(`${this.resourceUrl}/${getStudentIdentifier(student) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IStudent>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IStudent[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addStudentToCollectionIfMissing(studentCollection: IStudent[], ...studentsToCheck: (IStudent | null | undefined)[]): IStudent[] {
    const students: IStudent[] = studentsToCheck.filter(isPresent);
    if (students.length > 0) {
      const studentCollectionIdentifiers = studentCollection.map(studentItem => getStudentIdentifier(studentItem)!);
      const studentsToAdd = students.filter(studentItem => {
        const studentIdentifier = getStudentIdentifier(studentItem);
        if (studentIdentifier == null || studentCollectionIdentifiers.includes(studentIdentifier)) {
          return false;
        }
        studentCollectionIdentifiers.push(studentIdentifier);
        return true;
      });
      return [...studentsToAdd, ...studentCollection];
    }
    return studentCollection;
  }

  protected convertDateFromClient(student: IStudent): IStudent {
    return Object.assign({}, student, {
      birthDate: student.birthDate?.isValid() ? student.birthDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.birthDate = res.body.birthDate ? dayjs(res.body.birthDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((student: IStudent) => {
        student.birthDate = student.birthDate ? dayjs(student.birthDate) : undefined;
      });
    }
    return res;
  }
}
