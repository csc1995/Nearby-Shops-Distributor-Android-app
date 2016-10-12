package org.localareadelivery.distributorapp.zzDaggerDepricated;

import org.localareadelivery.distributorapp.zzDataRouters.ItemCategoryDataRouter;

import javax.inject.Singleton;
import dagger.Component;

/**
 * Created by sumeet on 19/5/16.
 */

@Singleton
@Component(modules = {DataModule.class})
public interface DataComponent {

    void Inject(ItemCategoryDataRouter dataRouter);

}