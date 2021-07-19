import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReportDto } from 'app/shared/dto/report-dto';

export type ReportDtoResponseType = HttpResponse<IReportDto>;

@Injectable({
  providedIn: 'root'
})
export class ReportsServceService {

  public resourceUrlStudent = this.applicationConfigService.getEndpointFor('api/students/report');
  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) { }

  studentReport(req?: any): Observable<ReportDtoResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReportDto>(this.resourceUrlStudent, { params: options, observe: 'response' })
  }

}
