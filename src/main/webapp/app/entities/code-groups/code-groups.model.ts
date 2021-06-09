export interface ICodeGroups {
  id?: number;
  code?: string | null;
  value?: string | null;
  description?: string | null;
  json?: string | null;
  priority?: number | null;
}

export class CodeGroups implements ICodeGroups {
  constructor(
    public id?: number,
    public code?: string | null,
    public value?: string | null,
    public description?: string | null,
    public json?: string | null,
    public priority?: number | null
  ) {}
}

export function getCodeGroupsIdentifier(codeGroups: ICodeGroups): number | undefined {
  return codeGroups.id;
}
