import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CodeGroupsDetailComponent } from './code-groups-detail.component';

describe('Component Tests', () => {
  describe('CodeGroups Management Detail Component', () => {
    let comp: CodeGroupsDetailComponent;
    let fixture: ComponentFixture<CodeGroupsDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CodeGroupsDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ codeGroups: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CodeGroupsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CodeGroupsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load codeGroups on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.codeGroups).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
