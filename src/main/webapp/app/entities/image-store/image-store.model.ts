export interface IImageStore {
  id?: number;
  name?: string | null;
  description?: string | null;
  base64?: string | null;
}

export class ImageStore implements IImageStore {
  constructor(public id?: number, public name?: string | null, public description?: string | null, public base64?: string | null) {}
}

export function getImageStoreIdentifier(imageStore: IImageStore): number | undefined {
  return imageStore.id;
}
