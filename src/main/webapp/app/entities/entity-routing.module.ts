import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'student',
        data: { pageTitle: 'learnMngtSysApp.student.home.title' },
        loadChildren: () => import('./student/student.module').then(m => m.StudentModule),
      },
      {
        path: 'code-groups',
        data: { pageTitle: 'learnMngtSysApp.codeGroups.home.title' },
        loadChildren: () => import('./code-groups/code-groups.module').then(m => m.CodeGroupsModule),
      },
      {
        path: 'image-store',
        data: { pageTitle: 'learnMngtSysApp.imageStore.home.title' },
        loadChildren: () => import('./image-store/image-store.module').then(m => m.ImageStoreModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
