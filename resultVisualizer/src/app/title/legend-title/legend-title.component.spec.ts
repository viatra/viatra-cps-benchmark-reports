import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LegendTitleComponent } from './legend-title.component';

describe('LegendTitleComponent', () => {
  let component: LegendTitleComponent;
  let fixture: ComponentFixture<LegendTitleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LegendTitleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LegendTitleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
