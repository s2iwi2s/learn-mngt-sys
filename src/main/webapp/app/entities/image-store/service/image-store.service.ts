import { Injectable } from '@angular/core'
import { HttpClient, HttpResponse } from '@angular/common/http'
import { Observable } from 'rxjs'

import { isPresent } from 'app/core/util/operators'
import { ApplicationConfigService } from 'app/core/config/application-config.service'
import { createRequestOption } from 'app/core/request/request-util'
import { IImageStore, getImageStoreIdentifier } from '../image-store.model'

export type EntityResponseType = HttpResponse<IImageStore>
export type EntityArrayResponseType = HttpResponse<IImageStore[]>

@Injectable({ providedIn: 'root' })
export class ImageStoreService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/image-stores')

  constructor (protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create (imageStore: IImageStore): Observable<EntityResponseType> {
    return this.http.post<IImageStore>(this.resourceUrl, imageStore, { observe: 'response' })
  }

  update (imageStore: IImageStore): Observable<EntityResponseType> {
    return this.http.put<IImageStore>(`${this.resourceUrl}/${getImageStoreIdentifier(imageStore) as number}`, imageStore, {
      observe: 'response'
    })
  }

  partialUpdate (imageStore: IImageStore): Observable<EntityResponseType> {
    return this.http.patch<IImageStore>(`${this.resourceUrl}/${getImageStoreIdentifier(imageStore) as number}`, imageStore, {
      observe: 'response'
    })
  }

  find (id: number): Observable<EntityResponseType> {
    return this.http.get<IImageStore>(`${this.resourceUrl}/${id}`, { observe: 'response' })
  }

  query (req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req)
    return this.http.get<IImageStore[]>(this.resourceUrl, { params: options, observe: 'response' })
  }

  delete (id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' })
  }

  addImageStoreToCollectionIfMissing (
    imageStoreCollection: IImageStore[],
    ...imageStoresToCheck: (IImageStore | null | undefined)[]
  ): IImageStore[] {
    const imageStores: IImageStore[] = imageStoresToCheck.filter(isPresent)
    if (imageStores.length > 0) {
      const imageStoreCollectionIdentifiers = imageStoreCollection.map(imageStoreItem => getImageStoreIdentifier(imageStoreItem)!)
      const imageStoresToAdd = imageStores.filter(imageStoreItem => {
        const imageStoreIdentifier = getImageStoreIdentifier(imageStoreItem)
        if (imageStoreIdentifier == null || imageStoreCollectionIdentifiers.includes(imageStoreIdentifier)) {
          return false
        }
        imageStoreCollectionIdentifiers.push(imageStoreIdentifier)
        return true
      })
      return [...imageStoresToAdd, ...imageStoreCollection]
    }
    return imageStoreCollection
  }

  findImage (id: string): Observable<EntityResponseType> {
    return this.http.get<IImageStore>(`${this.resourceUrl}/image/${id}`, { observe: 'response' })
  }
}
