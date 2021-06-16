/* eslint-disable @typescript-eslint/no-unsafe-return */
/* eslint-disable no-console */
/* eslint-disable @typescript-eslint/no-empty-function */
import { Injectable } from '@angular/core'
import { forkJoin } from 'rxjs'
import { map } from 'rxjs/operators'
import { PDFDocument, PDFPage } from 'pdf-lib';

import { ImageStoreService } from './image-store.service'

@Injectable({
  providedIn: 'root'
})
export class ExportService {
  constructor(protected imageStoreService: ImageStoreService) {
  }

  async export(docs: string[]): Promise<void> {
    const finalPdf = await PDFDocument.create();

    forkJoin(docs.map(id => this.imageStoreService.findImage(id)))
      .pipe(map((data: any[]) => data.reduce((ary: string[], resp: any) => [...ary, resp.body.base64], [])))
      .subscribe((base64PdfArray: string[]) => {
        console.log('base64PdfArray=>', base64PdfArray);
        Promise.all(base64PdfArray.map((base64: any) => PDFDocument.load(base64)))
          .then(pdfDocuments =>
            Promise.all(pdfDocuments.map((pdfDocument: any) => finalPdf.copyPages(pdfDocument, pdfDocument.getPageIndices())))
          )
          .then(pdfPagesArray => pdfPagesArray.reduce((acc, c) => {
            c.forEach((page: any) => acc.push(page));
            return acc;
          }, []))
          .then(copiedPages => copiedPages.forEach((page: any) => finalPdf.addPage(page)))
          .then(() => finalPdf.saveAsBase64())
          .then(finalBase64 => {
            const newWindow = window.open('', '_blank');
            newWindow?.document.write(
              "<iframe width='100%' height='100%' src='data:application/pdf;base64, " + encodeURI(finalBase64) + "'></iframe>"
            );
          })
          .catch(e => {
            console.error('downloadPdf Error', e);
          });
      })
  }
}
