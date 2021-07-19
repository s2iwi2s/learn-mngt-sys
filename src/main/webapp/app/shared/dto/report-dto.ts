
export interface IReportDto {
 base64Data: string;
 binaryData: string;
 filename: string;
}

export class ReportDto implements IReportDto {
 constructor(public base64Data: string, public binaryData: string, public filename: string) { }
}
