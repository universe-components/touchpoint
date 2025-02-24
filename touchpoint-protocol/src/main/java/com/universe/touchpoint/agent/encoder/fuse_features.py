import torch
import numpy as np

# 融合特征
def fuse_features(dinov2_features, siglip_features):
    # 投影层
    projection_layer = torch.nn.Linear(768, 512)  # 假设目标维度为 512
    projection_layer.eval()

    dinov2_projected = projection_layer(torch.tensor(dinov2_features)).numpy()
    siglip_projected = projection_layer(torch.tensor(siglip_features)).numpy()
    fused_features = np.concatenate((dinov2_projected, siglip_projected), axis=1)

    return fused_features