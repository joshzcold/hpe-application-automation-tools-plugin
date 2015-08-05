package com.hp.octane.plugins.jenkins.model.snapshots;

import com.hp.octane.plugins.jenkins.model.api.AbstractPhase;
import com.hp.octane.plugins.jenkins.model.pipelines.StructureItem;
import com.hp.octane.plugins.jenkins.model.pipelines.StructurePhase;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: gullery
 * Date: 08/01/15
 * Time: 23:15
 * To change this template use File | Settings | File Templates.
 */

@ExportedBean
public final class SnapshotPhase extends AbstractPhase<SnapshotItem> {
	private static final Logger logger = Logger.getLogger(SnapshotPhase.class.getName());

	public SnapshotPhase(StructurePhase structurePhase, HashMap<String, ArrayList<AbstractBuild>> invokedBuilds) {
		super(structurePhase.getName(), structurePhase.getBlocking());
		ArrayList<AbstractBuild> tmpBuilds;
		StructureItem[] structures = structurePhase.getJobs();
		SnapshotItem[] tmp = new SnapshotItem[structures.length];
		for (int i = 0; i < tmp.length; i++) {
			if (structures[i] != null) {
				tmpBuilds = invokedBuilds == null ? null : invokedBuilds.get(structures[i].getName());
				if (tmpBuilds == null || tmpBuilds.size() == 0) {
					tmp[i] = new SnapshotItem((AbstractProject) Jenkins.getInstance().getItem(structures[i].getName()), false);
				} else {
					tmp[i] = new SnapshotItem(tmpBuilds.get(0), false);
					tmpBuilds.remove(0);
				}
			} else {
				logger.warning("One of referenced jobs is null, your Jenkins config probably broken, skipping the build info for this job...");
			}
		}
		super.setItems(tmp);
	}

	@Exported(inline = true)
	public SnapshotItem[] getBuilds() {
		return super.getItems();
	}
}