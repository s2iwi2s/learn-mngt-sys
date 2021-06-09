import * as dayjs from 'dayjs';
import { ICodeGroups } from 'app/entities/code-groups/code-groups.model';

export interface IStudent {
  id?: number;
  lrn?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  birthDate?: dayjs.Dayjs | null;
  birthPlace?: string | null;
  contactNo?: string | null;
  address1?: string | null;
  address2?: string | null;
  city?: string | null;
  zipCode?: string | null;
  country?: string | null;
  fathersName?: string | null;
  fathersOccupation?: string | null;
  mothersName?: string | null;
  mothersOccupation?: string | null;
  guardianName?: string | null;
  gender?: ICodeGroups | null;
  parentCivilStatus?: ICodeGroups | null;
  course?: ICodeGroups | null;
}

export class Student implements IStudent {
  constructor(
    public id?: number,
    public lrn?: string | null,
    public firstName?: string | null,
    public lastName?: string | null,
    public birthDate?: dayjs.Dayjs | null,
    public birthPlace?: string | null,
    public contactNo?: string | null,
    public address1?: string | null,
    public address2?: string | null,
    public city?: string | null,
    public zipCode?: string | null,
    public country?: string | null,
    public fathersName?: string | null,
    public fathersOccupation?: string | null,
    public mothersName?: string | null,
    public mothersOccupation?: string | null,
    public guardianName?: string | null,
    public gender?: ICodeGroups | null,
    public parentCivilStatus?: ICodeGroups | null,
    public course?: ICodeGroups | null
  ) {}
}

export function getStudentIdentifier(student: IStudent): number | undefined {
  return student.id;
}
