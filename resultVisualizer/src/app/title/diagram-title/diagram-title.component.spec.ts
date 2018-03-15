import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DiagramTitleComponent } from './diagram-title.component';

describe('DiagramTitleComponent', () => {
  let component: DiagramTitleComponent;
  let fixture: ComponentFixture<DiagramTitleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DiagramTitleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiagramTitleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
