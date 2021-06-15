export interface IImageStore {
  id?: number;
  name?: string | null;
  description?: string | null;
  store?: string | null;
}

export class ImageStore implements IImageStore {
  constructor(public id?: number, public name?: string | null, public description?: string | null, public store?: string | null) {}
}

export function getImageStoreIdentifier(imageStore: IImageStore): number | undefined {
  return imageStore.id;
}
