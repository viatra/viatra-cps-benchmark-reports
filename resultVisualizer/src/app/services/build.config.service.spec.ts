import { TestBed, inject } from '@angular/core/testing';

import { Build.ConfigService } from './build.config.service';

describe('Build.ConfigService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [Build.ConfigService]
    });
  });

  it('should be created', inject([Build.ConfigService], (service: Build.ConfigService) => {
    expect(service).toBeTruthy();
  }));
});
