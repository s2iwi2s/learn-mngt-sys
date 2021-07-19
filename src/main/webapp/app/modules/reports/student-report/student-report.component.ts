/* eslint-disable @typescript-eslint/no-unnecessary-condition */
/* eslint-disable no-console */

import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { COMPANIES, COMPANY_NAMES } from 'app/app.constants';
import { IReportDto } from 'app/shared/dto/report-dto';
import Swal from 'sweetalert2';
import { ReportsServceService } from '../reports-servce.service';

@Component({
  selector: 'jhi-student-report',
  templateUrl: './student-report.component.html',
  styleUrls: ['./student-report.component.scss']
})
export class StudentReportComponent implements OnInit {

  companies: any = [];
  companyNames: any = COMPANY_NAMES;
  isLoading = false;

  searchForm: FormGroup = new FormGroup({
    companyId: new FormControl(''),
    fromDate: new FormControl(''),
    toDate: new FormControl(''),
  });

  constructor(private reportsServceService: ReportsServceService) {
    this.companies = COMPANIES.sort((a: number, b: number) => a - b);
  }

  ngOnInit(): void {
    ;
  }


  search(): void {
    this.isLoading = true;
    const formData = this.searchForm.value;
    console.log('formData=>', formData);
    if (!formData.companyId || !formData.toDate) {
      Swal.fire({
        text: 'Invalid request',
        icon: 'error',
      });
      this.isLoading = false;
      return;
    }
    this.reportsServceService.studentReport({ companyId: formData.companyId, fromDate: formData.fromDate, toDate: formData.toDate })
      .subscribe((res: HttpResponse<IReportDto>) => {
        const response = res.body;
        const fileName: string = response?.filename ?? '';
        this.createAndDownloadBlobFile(response?.binaryData ?? '', fileName, 'xls');

        this.isLoading = false;
      }, this.throwError);
  }

  createAndDownloadBlobFile(base64Data: string, filename: string, extension: string): void {
    const raw = window.atob(base64Data);
    // Create an array to store the decoded data
    const uInt8Array = new Uint8Array(raw.length);
    // blob can only receive binary encoding, need to talk about base64 converted to binary and then stuffed
    for (let i = 0; i < raw.length; ++i) {
      uInt8Array[i] = raw.charCodeAt(i);
    }

    const blob = new Blob([uInt8Array]);
    const fileName = `${filename}.${extension}`;
    if (navigator.msSaveBlob) {
      // IE 10+
      navigator.msSaveBlob(blob, fileName);
    } else {
      const link = document.createElement('a');
      // Browsers that support HTML5 download attribute
      if (link.download !== undefined) {
        const url = URL.createObjectURL(blob);
        link.setAttribute('href', url);
        link.setAttribute('download', fileName);
        link.style.visibility = 'hidden';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      }
    }
  }

  protected throwError(error: HttpErrorResponse): void {
    console.error(`error: status=${error.status}, statusText=${error.statusText}`);
    this.isLoading = false;

    let msg = `Status: ${error.status}, message: ${error.statusText}`;
    msg = 'There was an error fetching the data from the server';

    if (error.error) {
      msg = error.error.message;
    }

    Swal.fire({
      text: msg,
      icon: 'error',
    });
    this.isLoading = false;
  }
}
