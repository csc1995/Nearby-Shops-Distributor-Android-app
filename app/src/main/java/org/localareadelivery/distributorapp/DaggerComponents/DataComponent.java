package org.localareadelivery.distributorapp.DaggerComponents;

import org.localareadelivery.distributorapp.DaggerModules.DataModule;
import org.localareadelivery.distributorapp.DataRouters.ItemCategoryDataRouter;
import org.localareadelivery.distributorapp.Model.ItemCategory;
import org.localareadelivery.distributorapp.StandardInterfaces.DataRouter;
import org.localareadelivery.distributorapp.addItems.ItemCategories.ItemCategories;

import javax.inject.Singleton;
import dagger.Component;

/**
 * Created by sumeet on 19/5/16.
 */

@Singleton
@Component(modules = {DataModule.class})
public interface DataComponent {

    void Inject(ItemCategoryDataRouter dataRouter);

    void Inject(ItemCategories itemCategories);

}