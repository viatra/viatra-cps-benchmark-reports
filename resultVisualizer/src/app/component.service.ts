import { Injectable ,EventEmitter } from '@angular/core';

@Injectable()
export class ComponentService {
  private _componentUpdate : EventEmitter<ComponentUpdateEvent>;
  private _classUpdate : EventEmitter<ClassUpdateEvent>;
  constructor() {
    this._componentUpdate = new EventEmitter<ComponentUpdateEvent>();
    this._classUpdate = new EventEmitter<ClassUpdateEvent>();
   }

get ComponentUpdate(){
  return this._componentUpdate;
}

get ClassUpdate(){
  return this._classUpdate;
}

public updateComponent(component: string, hide: boolean){
    this._componentUpdate.emit(new ComponentUpdateEvent(component,hide));
  }

public updateClass(ngclass: string){
  this._classUpdate.emit(new ClassUpdateEvent(ngclass))
}
}



export class ComponentUpdateEvent{
  constructor(public component: string, public hide :boolean){}
}

export class ClassUpdateEvent{
  constructor(public ngClass : string){}
}